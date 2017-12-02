(set-env!
 :source-paths #{"src"}
 :resource-paths #{"html"}

 :dependencies '[
                 [org.clojure/clojurescript "RELEASE"]
                 [pandeiro/boot-http "RELEASE"]
                 [quil "RELEASE"]

                 [org.clojure/clojure "RELEASE" :scope "test"]
                 [adzerk/boot-cljs "RELEASE" :scope "test"]
                 [adzerk/boot-cljs-repl "RELEASE" :scope "test"]
                 [adzerk/boot-reload "RELEASE" :scope "test"]
                 [com.cemerick/piggieback "RELEASE" :scope "test"]
                 [weasel "RELEASE" :scope "test"]
                 [org.clojure/tools.nrepl "RELEASE" :scope "test"]])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload :refer [reload]]
         '[pandeiro.boot-http :refer [serve]])

(deftask build
  "The `build` task contains all necessary steps to produce a build
  You can use 'profile-tasks' like `production` and `development`
  to change parameters (like optimizations level of the cljs compiler"
  []
  (comp (notify {:audible true :visible true})
        (cljs)
        ))

(deftask run
  "The `run` task wraps the building of your application in some
   useful tools for local development: an http server, a file watcher
   a ClojureScript REPL and a hot reloading mechanism"
  []
  (comp (serve)
        (watch)
        (cljs-repl)

        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced :source-map true})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none :source-map true}
                 reload {:on-jsload '{{name}}.sketch/{{name}}})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

(deftask prod
  "Simple alias to run application in production mode"
  [d directories PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq directories) directories #{"target"})]
    (comp (production)
       (cljs)
       (target :dir dir)))
  )

(deftask github-pages
  "Build a production version to github-pages `docs/` directory"
  []
  (comp (prod :directories ["docs/"])))
