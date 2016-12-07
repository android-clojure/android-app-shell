(defproject droid1/droid1 "0.1.0-SNAPSHOT"
  :description "FIXME: Android project description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :global-vars {clojure.core/*warn-on-reflection* true}
  ;; :java-cmd "/home/a/opt/jre/bin/java"
  :java-cmd "/usr/lib/jvm/java-7-openjdk-amd64/bin/java"

  :source-paths ["src/clojure" "src"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :plugins [[lein-droid "0.4.4"]]
  ;; :plugins [[lein-droid "0.4.6-SNAPSHOT"]]

  :dependencies [[org.clojure-android/clojure "1.7.0-r4"]
                 ;; [com.android.support/recyclerview-v7 "18.0.0"]
                 [neko/neko "4.0.0-alpha5"]]

  :profiles {:default [:dev]

             :dev
             [:android-common :android-user
              {:dependencies [[org.clojure/tools.nrepl "0.2.12"]
                              #_[org.clojure/tools.nrepl "0.2.10"]]
               :target-path "target/debug"
               :android {:aot :all-with-unused
                         :manifest-options {:app-name "droid1 (debug)"}
                         ;; Uncomment to be able install debug and release side-by-side.
                         ;; :rename-manifest-package "com.example.droid1.debug"
                         }}
              ]

             :release
             [:android-common
              {:target-path "target/release"
               :android
               { ;; :keystore-path "/home/user/.android/private.keystore"
                ;; :key-alias "mykeyalias"
                ;; :sigalg "MD5withRSA"

                :use-debug-keystore true
                :ignore-log-priority [:debug :verbose]
                :aot :all
                :build-type :release}}]

             :lean
             [:release
              {:dependencies ^:replace [[org.skummet/clojure "1.7.0-r2"]
                                        [neko/neko "4.0.0-alpha5"]]
               :exclusions [[org.clojure/clojure]
                            [org.clojure-android/clojure]]
               :jvm-opts ["-Dclojure.compile.ignore-lean-classes=true"]
               :android {:lean-compile true
                         :proguard-execute true
                         :proguard-conf-path "build/proguard-minify.cfg"}}]}

  :android { ;; Specify the path to the Android SDK directory.
            ;; :sdk-path ".../android-sdk-linux"

            ;; Increase this value if dexer fails with OutOfMemoryException.
            :dex-opts ["-JXmx4096M" "--incremental"]

            :target-version "18"
            :aot-exclude-ns ["clojure.parallel" "clojure.core.reducers"
                             "cider.nrepl" "cider-nrepl.plugin"
                             "cider.nrepl.middleware.util.java.parser"
                             #"cljs-tooling\..+"]}

  ;; :android-user {:dependencies [[cider/cider-nrepl "0.9.1"]]
  ;;                :android {:aot-exclude-ns ["cider.nrepl.middleware.util.java.parser"
  ;;                                           "cider.nrepl" "cider-nrepl.plugin"]}
  ;;                :repl-options {:host "0.0.0.0" :port 9999}}
  )
