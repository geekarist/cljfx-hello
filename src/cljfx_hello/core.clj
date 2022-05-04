(ns cljfx-hello.core
  (:require [cljfx.api :as fx]
            [clojure.core.cache :as cache])
  (:gen-class))

(def context
  (atom (fx/create-context
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

(defn- effect1! [arg1]
  (println "Applying effect 1 on" arg1))

(defn- effect2! [arg2]
  (println "Applying effect 2 on" arg2))

(def actual-event-handler
  (-> event-handler
      (fx/wrap-effects {:eff/effect1 effect1!
                        :eff/effect2 effect2!})))

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
  (fx/create-app context
                 :event-handler actual-event-handler
                 :desc-fn root-view))