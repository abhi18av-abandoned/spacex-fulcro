(ns app.utils
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [clojure.string :as str]))


;;;;;;;;;;;;;;;;;
;; FULCRO
;;;;;;;;;;;;;;;;;

#_(js/console.log "%cExtra Large Yellow Text with Red Background", "background: red; color: yellow; font-size: large")

(defn clog
  "
  The colors have been taken from https://developer.mozilla.org/en-US/docs/Web/CSS/color_value
  "
  [{:keys [message props color] :or {message "Hello, World!" color "green" props {}}}]
  (js/console.log (str "%c" message), (str "color: " color "; font-weight: bold; font-size: small;"))
  (js/console.log props))

(comment
  (clog {:message "Hello, CLog" :color "blue"})
  )



(defn get-components-that-query-for-a-prop
  [App prop]
  (reduce (fn [mounted-instances cls]
            (concat mounted-instances
                    (comp/class->all App (comp/registry-key->class cls))))
          []
          (comp/prop->classes App prop)))


;;;;;;;;;;;;;;;;;
;; PATHOM
;;;;;;;;;;;;;;;;;

(defn adapt-key [k]
  (str/replace k #"_" "-"))

(defn set-ns
  "Set the namespace of a keyword"
  [ns kw]
  (keyword ns (adapt-key (name kw))))

(defn set-ns-seq
  "Set the namespace for all keywords in a collection. The collection kind will
  be preserved."
  [ns s]
  (into (empty s) (map #(set-ns ns %)) s))

(defn set-ns-x
  "Set the namespace of a value. If sequence will use set-ns-seq."
  [ns x]
  (if (coll? x)
    (set-ns-seq ns x)
    (set-ns ns x)))

(defn namespaced-keys
  "Set the namespace of all map keys (non recursive)."
  [e ns]
  (reduce-kv
    (fn [x k v]
      (assoc x (set-ns ns k) v))
    {}
    e))

(defn pull-key
  "Pull some key"
  [x key]
  (-> (dissoc x key)
      (merge (get x key))))

(defn pull-namespaced
  "Pull some key, updating the namespaces of it"
  [x key ns]
  (-> (dissoc x key)
      (merge (namespaced-keys (get x key) ns))))


(defn update-if [m k f & args]
  (if (contains? m k)
    (apply update m k f args)
    m))




