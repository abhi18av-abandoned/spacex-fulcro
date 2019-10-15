(ns app.ui
  (:require
    ;; project libs
    ;[app.secrets :as secrets]
    [app.utils :as utils :refer [clog]]
    [app.pathom :as p :refer [pathom-api]]
    [app.temp-db :refer [all-launches]]

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
;; FirstComponent Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


#_(m/defmutation update-temp-db [_]
  (action [{:keys [state]}]
          (clog {:message "[LatestLaunch] MUTATION update-temp-db" :color "magenta" :props state})
          (df/load! this)))


(comment

  (df/load! APP :latest-launch LatestLaunch)

  (pathom-api {} [:spacex/latest-launch])

  (pathom-api {} [{:spacex/latest-launch [:spacex.launch/mission-name]}])

  '())




(defsc LatestLaunch [this props]
  {:query              [:latest-launch]
   :ident              :latest-launch
   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[FirstComponent]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstComponent] MOUNTED" :props p :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstComponent]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/button :.ui.button.primary
                {:onClick (fn []
                            (df/load! this (comp/get-ident this) LatestLaunch))}
                "Fetch latest launch")))


(def ui-latest-launch (comp/factory LatestLaunch {:keyfn :latest-launch}))


(comment

  (comp/get-query LatestLaunch)

  '())


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this props]
  {:query              [:root (comp/get-query LatestLaunch)]
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
    (ui-latest-launch props)))


(comment

  (comp/get-query Root)

  '())



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



