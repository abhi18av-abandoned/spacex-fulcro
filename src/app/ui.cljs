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
    [com.fulcrologic.fulcro.mutations :as m]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; COMPONENTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FirstComponent Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def temp-db-data (atom {}))

(m/defmutation update-temp-db [_]
  (action [{:keys [state]}]
          (clog {:message "[FirstComponent] MUTATION update-temp-db" :color "magenta" :props state})
          (reset! temp-db-data all-launches)))



(defsc FirstComponent [this props]
  {#_#_:query []
   #_#_:ident []
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
    (dom/div)))


(def ui-first-component (comp/factory FirstComponent))





;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this props]
  {#_#_:query []
   #_#_:ident []

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
    (dom/h1 :.ui.header "Hello, Fulcro!")
    (ui-first-component props)))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



