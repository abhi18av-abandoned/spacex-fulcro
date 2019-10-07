(ns app.client
  (:require

    ;; project libs
    [app.application :refer [APP]]
    [app.ui :as ui]
    [app.utils :as utils :refer [clog]]

    ;; internal libs
    [com.fulcrologic.fulcro.application :as app]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:export init []
  (app/mount! APP ui/Root "app")
  (clog {:message "[APP]: INITIALIZED" :color "orange"}))


