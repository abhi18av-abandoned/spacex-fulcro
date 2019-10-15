(ns app.ui
  (:require
    ;; project libs
    ;[app.secrets :as secrets]
    [app.utils :as utils :refer [clog]]
    [app.pathom :as p :refer [pathom-api]]
    [app.temp-db :refer [latest-launch-data]]

    ;; external libs
    [clojure.string :as str]

    ;; internal libs
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.mutations :as m]
    [com.fulcrologic.fulcro.data-fetch :as df]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; COMPONENTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this props]
  {:query              [{:spacex/latest-launch [:spacex.launch/mission-name]}]
   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[Root]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root] MOUNTED \nTimeStamp :" :props (js/Date.) :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/h1 :.ui.header "SpaceX - Fulcro Project")
    (dom/button :.ui.button.primary
                #_{:onClick (fn []

                            (df/load! this (comp/get-ident this)
                                      Root
                                      {:target [:spacex/latest-launch]})
                            #_(pathom-api {} [:spacex/latest-launch]))}
                "Fetch latest launch")
    (dom/div :.ui.segment "Mission Name")))


(comment







  (comp/get-query Root)

  (df/load! app.application/APP :spacex/latest-launch Root {:target [:spacex/latest-launch]})

  (pathom-api {} [:spacex/latest-launch])

  (pathom-api {} [{:spacex/latest-launch [:spacex.launch/mission-name]}])


  '())



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



