(ns jingles.dtrace
  (:refer-clojure :exclude [get list])
  (:require [jingles.api :as api]
            [jingles.list :as jlist]
            [jingles.state :refer [set-state!]]))

(def root :dtrace)

(def config {:fields {:name {:id :name :title "Name" :key :name}
                      :uuid {:id :uuid :title "UUID" :key :uuid}}
             :root root
             :title "D-Trace"})

(set-state! [root :fields] (reduce #(assoc-in %1 [%2 :show] true) {} (keys (:fields config))))

(def list-fields
  "uuid,name")

(def list (partial api/list root list-fields))

(def get (partial api/get root))

(defn list-view [app]
  (jlist/view config app))

(defn show-view [app]
  (. js/JSON (stringify (clj->js (get-in app [root :element])))))

(defn view [app]
  (condp = (:view app)
    :list (list-view app)
    :show (show-view app)))