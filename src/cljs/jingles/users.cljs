(ns jingles.users
  (:refer-clojure :exclude [get list])
  (:require
   [om.core :as om :include-macros true]
   [jingles.list :as jlist]
   [jingles.users.api :refer [root] :as users]
   [om-bootstrap.random :as r]
   [jingles.users.view :as view]
   [jingles.fields :refer [mk-config]]
   [jingles.utils :refer [initial-state]]
   [jingles.state :refer [set-state!]]))


(defn actions [{uuid :uuid}]
  [["Delete" #(users/delete uuid)]])

(def config (mk-config
             root "Users" actions
             :name {:title "Name" :key :name :order -10}
             :org {:title "Organisation" :key :org :order -10}
             ))

(set-state! [root :fields] (initial-state config))

(defn render [data owner opts]
  (reify
    om/IDisplayName
    (display-name [_]
      "userviewc")
    om/IWillMount
    (will-mount [_]
      (om/update! data [root :filter] "")
      (om/update! data [root :filted] [])
      (om/update! data [root :sort] {})
      (users/list data))
    om/IRenderState
    (render-state [_ _]
      (condp = (:view data)
        :list (om/build jlist/view data {:opts {:config config}})
        :show (om/build view/render data {}))))) 
