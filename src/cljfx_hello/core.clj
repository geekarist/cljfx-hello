(ns cljfx-hello.core
  (:require [cljfx.api :as cljfx]
            [clojure.core.cache :as cache])
  (:gen-class))

(def context
  (atom (cljfx/create-context
         {}
         #(cache/lru-cache-factory % :threshold 4096))))

(defmulti event-handler :event/type)

(defmethod event-handler :evt/button-clicked
  [args]
  (println "Button clicked with args:" args))

(defmethod event-handler :default [{:keys [fx/event fx/context] :as arg}]
  (println "Handling arg:" arg)
  (println "Event:" event)
  (println "Context:" context))

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