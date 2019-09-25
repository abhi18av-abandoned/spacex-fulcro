(ns app.client
  (:require
    ;; project libs
    [app.utils :as utils :refer [clog]]
    [app.pathom :as p :refer [spacex-api]]

    ;; external libs
    [clojure.string :as str]

    ;; internal libs
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.mutations :as m]))

;; TODO add workspaces to the project

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; UTILS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SingleLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TODO This is the UI part of the component
(defsc SingleLaunch [this props]
  {:query         []
   :initial-state {}}
  (comp/fragment
    ()))

(def ui-single-launch (comp/factory SingleLaunch))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LatestLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(m/defmutation fetch-latest-launch [_]
  (action [{:keys [state]}]
          (clog {:message "[LatestLaunch] MUTATION fetch-latest-launch" :color "magenta" :props state})
          (spacex-api {} [:spacex/latest-launch])))


(defsc LatestLaunch [this props]
  {:query         []
   :initial-state {}}
  (dom/button :.ui.button.primary
              {:onClick #(comp/transact! this [(fetch-latest-launch)])}
              "SpaceX: Fetch latest launch!"))

(def ui-latest-launch (comp/factory LatestLaunch))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this props]
  {:query              []
   :initial-state      {}

   :initLocalState     (fn [this]
                         (clog {:message "[Root]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root] MOUNTED (with TimeStamp)" :props (js/Date.) :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[Root]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/h1 :.ui.header "Hello, Fulcro!")
    (ui-latest-launch)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP definition and init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defonce APP (app/fulcro-app))

(defn ^:export init []
  (app/mount! APP Root "app"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(comment
  (shadow/repl :main)

  ;; js hard reset
  (.reload js/location true)

  (::app/state-atom APP)

  (-> APP
      (::app/state-atom)
      deref)

  (app/schedule-render! APP {:force-root? true})

  (reset! (::app/state-atom APP) {})

  (app/current-state APP))

