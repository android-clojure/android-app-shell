(ns com.example.droid1.inspector
  (:require [clojure.reflect :refer [reflect]]
            [clojure.set :as set]))

(defmacro inspect-fields [obj ^Class class]
  `{~@(->> (eval class)
           reflect
           :members
           (filter #(and (instance? clojure.reflect.Field %)
                         (empty? (set/intersection #{:static :final :private} (:flags %)))))
           (map :name)
           (map (juxt keyword #(symbol (str \. %))))
           (mapcat (fn [[key field]]
                     [key `(~field ~obj)])))
    ~@[]})

(defmacro inspect-getters [obj ^Class class]
  `{~@(->> (eval class)
           clojure.reflect/reflect
           :members
           (filter #(and (instance? clojure.reflect.Method %)
                         ((:flags %) :public)
                         (not ((:flags %) :static))
                         (re-find #"\A(?:get|is)[A-Z]" (str (:name %)))
                         (empty? (:parameter-types %))))
           (map (juxt #(keyword (second (re-find #"\A(?:get|is)(.+)\z" (str (:name %)))))
                      #(symbol (str \. (:name %)))))
           (mapcat (fn [[key getter]]
                     [key `(~getter ~obj)])))
    ~@[]})

(defn constants-of-class [^Class class]
  (->> class
       reflect
       :members
       (filter #(and (instance? clojure.reflect.Field %)
                     (set/superset? (:flags %) #{:static :final})))
       (map :name)))

(defmacro constant-mapper [class & [constants]]
  (let [pairs (->> (cond (seq? constants)                              constants
                         (instance? java.util.regex.Pattern constants) (->> (constants-of-class (eval class))
                                                                            (filter #(re-find constants (str %))))
                         :else                                         (constants-of-class (eval class)))
                   (keep (fn [sym]
                           (let [val `(. ~class ~sym)]
                             (try (eval val)
                                  [val (keyword sym)]
                                  (catch Exception e)))))
                   (apply concat))]
    `{~@pairs ~@[]}))

(defmacro keywordize-bitset [bitset class & [constants]]
  `(cond-> []
     ~@(->> (cond (seq? constants)
                  constants

                  (instance? java.util.regex.Pattern constants)
                  (->> (constants-of-class (eval class))
                       (filter #(re-find constants (str %))))

                  :else
                  (constants-of-class (eval class)))
            (keep (fn [sym]
                    (let [bit `(. ~class ~sym)]
                      (try (eval bit)
                           `[(not (zero? (bit-and ~bitset ~bit)))
                             (conj ~(keyword sym))]
                           (catch Exception e)))))
            (apply concat))))

;;; def*
(defmacro deffield-inspector [^clojure.lang.Symbol name ^Class class]
  `(defn- ~name [obj#]
     (inspect-fields obj# ~class)))
