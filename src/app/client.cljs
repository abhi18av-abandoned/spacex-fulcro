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
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




(defsc Root [this {:keys [:spacex/latest-launch] :as props}]
  {:query              [:spacex/latest-launch #_{:spacex/latest-launch (comp/get-query LatestLaunch)}]
   :ident              :spacex/latest-launch
   :initial-state      {:spacex/latest-launch {:spacex.launch/flight-number      83,
                                               :spacex.launch.links/wikipedia    "https://en.wikipedia.org/wiki/Spacecom",
                                               :spacex.launch/ships              ["GOMSTREE"
                                                                                  "GONAVIGATOR"],
                                               :spacex.launch.first-stage/cores  [{:spacex.core/reused          true,
                                                                                   :spacex.core/core-serial     "B1047",
                                                                                   :spacex.core/landing-vehicle nil}],
                                               :spacex.launch/launch-date-utc    "2019-08-06T22:52:00.000Z",
                                               :spacex.rocket/rocket-id          "falcon9",
                                               :spacex.launch/mission-name       "Amos-17",
                                               :spacex.launch.second-stage/block 5}}
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
    #_(ui-first-stage cores)))


(comment


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

