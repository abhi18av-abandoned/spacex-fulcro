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
;; LatestLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




(defsc FirstStageCores [this {:keys [:spacex.core/core-serial] :as props}]
  {:query              [:spacex.core/core-serial]
   :ident              :spacex.core/core-serial
   :initial-state      {:spacex.core/core-serial "b1047"}
   :initlocalstate     (fn [this]
                         (clog {:message "[FirstStageCores]: initlocalstate" :color "teal"}))
   :componentdidmount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstStageCores] MOUNTED" :props p :color "green"})))
   :componentdidupdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[FirstStageCores]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/div "Core serial: " core-serial)))


(def ui-first-stage-cores (comp/factory FirstStageCores (:keyfn :spacex.core/core-serial)))

(comment

  (comp/get-query FirstStageCores)

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (app/schedule-render! APP)

  (merge/merge-component! APP FirstStageCores {:spacex.core/reused      true,
                                               :spacex.core/core-serial "b1047"})

  '())



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LatestLaunch Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




(defsc LatestLaunch [this {:keys [:spacex.launch/flight-number
                                  :spacex.launch/mission-name
                                  :spacex.launch.first-stage/cores]
                           :as   props}]

  {:query              [:spacex.launch/flight-number
                        :spacex.launch/mission-name
                        {:spacex.launch.first-stage/cores (comp/get-query FirstStageCores)}]

   :ident              :spacex.launch/flight-number

   :initial-state      {:spacex.launch/flight-number 83
                        :spacex.launch/mission-name  "Amos-17"}

   :initlocalstate     (fn [this]
                         (clog {:message "[LatestLaunch]: initlocalstate" :color "teal"}))
   :componentdidmount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LatestLaunch] MOUNTED" :props p :color "green"})))
   :componentdidupdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[LatestLaunch]: UPDATED" :color "blue" :props p})))}
  (comp/fragment
    (dom/div "Mission name: " mission-name)
    (dom/div "Flight number: " flight-number)
    (ui-first-stage-cores cores)))


(def ui-latest-launch (comp/factory LatestLaunch (:keyfn :spacex.launch/flight-number)))

(comment

  (comp/get-query LatestLaunch)


  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (app/schedule-render! APP)


  (merge/merge-component! APP LatestLaunch {:spacex.launch/flight-number      84,
                                            :spacex.launch.links/wikipedia    "https://en.wikipedia.org/wiki/spacecom",
                                            :spacex.launch/ships              ["gomstree"
                                                                               "gonavigator"],
                                            :spacex.launch.first-stage/cores  [{:spacex.core/reused          true,
                                                                                :spacex.core/core-serial     "b1047",
                                                                                :spacex.core/landing-vehicle nil}],
                                            :spacex.launch/launch-date-utc    "2019-08-06t22:52:00.000z",
                                            :spacex.rocket/rocket-id          "Falcon9",
                                            :spacex.launch/mission-name       "Amos-17",
                                            :spacex.launch.second-stage/block 5})


  '())

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




(defsc Root [this {:keys [:spacex/latest-launch] :as props}]
  {:query              [{:spacex/latest-launch (comp/get-query LatestLaunch)}]
   #_#_:initial-state [{:spacex/latest-launch (comp/get-initial-state LatestLaunch)}]

   :initial-state      {:spacex/latest-launch {#_#_:spacex.launch/flight-number 83,
                                               #_#_:spacex.launch/mission-name "Amos-17",
                                               :spacex.launch.links/wikipedia    "https://en.wikipedia.org/wiki/Spacecom",
                                               :spacex.launch/ships              ["GOMSTREE"
                                                                                  "GONAVIGATOR"],
                                               :spacex.launch.first-stage/cores  [{:spacex.core/reused          true,
                                                                                   :spacex.core/core-serial     "B1047",
                                                                                   :spacex.core/landing-vehicle nil}],
                                               :spacex.launch/launch-date-utc    "2019-08-06T22:52:00.000Z",
                                               :spacex.rocket/rocket-id          "falcon9",
                                               :spacex.launch.second-stage/block 5}}

   :initlocalstate     (fn [this]
                         (clog {:message "[root]: initlocalstate" :color "teal"}))
   :componentdidmount  (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[root] mounted (with timestamp)" :props (js/date.) :color "green"})))
   :componentdidupdate (fn [this]
                         (let [p (comp/props this)]
                           (clog {:message "[root]: updated" :color "blue" :props p})))}
  (comp/fragment
    (dom/h1 :.ui.header "Hello, Fulcro!")
    (ui-latest-launch latest-launch)))


(comment


  (comp/get-query Root)


  (def init-db {:spacex/latest-launch {:spacex.launch/flight-number      83,
                                       :spacex.launch.links/wikipedia    "https://en.wikipedia.org/wiki/Spacecom",
                                       :spacex.launch/ships              ["GOMSTREE"
                                                                          "GONAVIGATOR"],
                                       :spacex.launch.first-stage/cores  [{:spacex.core/reused          true,
                                                                           :spacex.core/core-serial     "B1047",
                                                                           :spacex.core/landing-vehicle nil}],
                                       :spacex.launch/launch-date-utc    "2019-08-06T22:52:00.000Z",
                                       :spacex.rocket/rocket-id          "falcon9",
                                       :spacex.launch/mission-name       "Amos-17",
                                       :spacex.launch.second-stage/block 5}})

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

