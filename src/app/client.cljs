(ns app.client
  (:require
    ;; project libs
    [app.utils :as utils :refer [clog]]
    [app.pathom :as p :refer [spacex-api]]
    [app.temp-db :refer [latest-launch]]

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

;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; SingleLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;(defsc SingleLaunch [this props]
;  {:query              []
;   :initial-state      {}
;   :initLocalState     (fn [this]
;                         (clog {:message "[SingleLaunch]: InitLocalState" :color "teal"}))
;   :componentDidMount  (fn [this]
;                         (let [p (comp/props this)]
;                           (clog {:message "[SingleLaunch] MOUNTED" :props p :color "green"})))
;   :componentDidUpdate (fn [this]
;                         (let [p (comp/props this)]
;                           (clog {:message "[SingleLaunch]: UPDATED" :color "blue" :props p})))}
;  (comp/fragment
;    (dom/div :.ui.segment "Single Launch" props)))
;
;(def ui-single-launch (comp/factory SingleLaunch))
;
;
;(comment
;
;  (reset! latest-launch-data {})
;
;  (comp/transact! APP [(update-latest-launch)])
;
;  @latest-launch-data
;
;  (merge/merge-component! SingleLaunch @latest-launch-data)
;
;  (app/schedule-render! APP)
;
;  )
;




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LaunchFirstStage Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc LaunchFirstStage [this {:keys [spacex.core/core-serial] :as props}]
  {#_#_:query []
   #_#_:ident []
   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[LaunchFirstStage]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LaunchFirstStage] MOUNTED" :props p :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LaunchFirstStage]: UPDATED" :color "blue" :props p})))}
  (comp/fragment

    (dom/div :.ui.segment "Core Serial " core-serial)))


(def ui-launch-first-stage (comp/factory LaunchFirstStage {:keyfn :spacex.core/core-serial}))


(comment

  (comp/transact! APP [(update-latest-launch)])

  @latest-launch-data

  (reset! latest-launch-data {})

  (reset! (::app/state-atom APP) @latest-launch-data)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (reset! (::app/state-atom APP) {:one 1})

  (app/schedule-render! APP)

  )



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LatestLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(m/defmutation fetch-latest-launch [_]
  (action [{:keys [state]}]
          (clog {:message "[LatestLaunch] MUTATION fetch-latest-launch" :color "magenta" :props state})
          (spacex-api {} [:spacex/latest-launch])))


(def latest-launch-data (atom {}))

(m/defmutation update-latest-launch [_]
  (action [{:keys [state]}]
          (clog {:message "[LatestLaunch] MUTATION update-latest-launch" :color "magenta" :props state})
          (reset! latest-launch-data latest-launch)))

(comment
  (comp/transact! APP [(update-latest-launch)])
  @latest-launch-data

  (app/schedule-render! APP)
  )

(defsc LatestLaunch [this {:keys [spacex.launch/flight-number
                                  spacex.launch.first-stage/cores] :as props}]
  {#_#_:query []
   #_#_:ident []
   :initial-state      {}
   :initLocalState     (fn [this]
                         (clog {:message "[LatestLaunch]: InitLocalState" :color "teal"}))
   :componentDidMount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LatestLaunch] MOUNTED" :props p :color "green"})))
   :componentDidUpdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LatestLaunch]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (if flight-number
      (dom/div :.ui.segment "Flight number " flight-number))
    (map ui-launch-first-stage cores)))


(def ui-latest-launch (comp/factory LatestLaunch {:keyfn :spacex.launch/flight-number}))


(comment

  (comp/transact! APP [(update-latest-launch)])

  @latest-launch-data

  (reset! latest-launch-data {})

  (reset! (::app/state-atom APP) @latest-launch-data)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (reset! (::app/state-atom APP) {:one 1})

  (app/schedule-render! APP)

  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this {:keys [:spacex/latest-launch] :as props}]
  {#_#_:query []
   #_#_:ident []

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
    (ui-latest-launch latest-launch)))


(comment

  (comp/transact! APP [(update-latest-launch)])

  @latest-launch-data

  (reset! latest-launch-data {})

  (reset! (::app/state-atom APP) @latest-launch-data)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (reset! (::app/state-atom APP) {:one 1})

  (app/schedule-render! APP)

  )



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

  (-> APP
      (::app/state-atom)
      deref)

  (app/schedule-render! APP {:force-root? true})

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  )

