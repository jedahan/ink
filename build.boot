(def project 'ink/boot-template)
(def version "0.1.1")

(set-env! :resource-paths #{"resources" "src"}
          ;; uncomment this if you write tests for your template:
          ;; :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [boot/new "RELEASE"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [adzerk/bootlaces "RELEASE"]])
(task-options!
 pom {:project     project
      :version     version
      :description "Simple quil template for sketching with boot"
      :url         "http://jedahan.com/ink"
      :scm         {:url "https://github.com/jedahan/ink"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(require '[adzerk.boot-test :refer [test]]
         '[adzerk.bootlaces :refer :all]
         '[boot.new :refer [new]])

(bootlaces! version)

(deftask deploy [] (comp (build-jar) (push-release)))

