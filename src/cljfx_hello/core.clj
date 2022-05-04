(ns cljfx-hello.core
  (:require [cljfx.api :as cljfx]
            [clojure.core.cache :as cache])
  (:gen-class))

(def context
  (atom (cljfx/create-context
         {}
         #(cache/lru-cache-factory % :threshold 4096))))

(defn event-handler [{:keys [fx/event fx/context] :as _arg-map}]
  (println "Handling event:" event)
  {:context context})

(defn root-view [arg]
  (println "Rendering:" arg)
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :children [{:fx/type :text
                              :text "Hello 🙂"}
                             {:fx/type :button
                              :text "Click me!"
                              :on-action {:event/type :evt/button-clicked}}]}}})

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def app
  (cljfx/create-app context
                    :event-handler event-handler
                    :desc-fn root-view))
(comment
  (-main))