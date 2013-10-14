(defproject elastacloud-servicebusspout "0.0.1-SNAPSHOT"
  :java-source-paths ["src"]
  :aot :all
  :repositories {
                  "local" ~(str (.toURI (java.io.File. "maven_repository")))
                  }

  :dependencies [
                  [com.microsoft.windowsazure/microsoft-windowsazure-api "0.4.5"]
                  [junit/junit "4.11"]
                  [org.mockito/mockito-all "1.8.4"]
                  [storm/storm "0.8.2"]
                  ]

  :profiles {:dev
             {:dependencies [[storm "0.8.2"]]}}

  :min-lein-version "2.0.0"
  )