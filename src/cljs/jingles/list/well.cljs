(ns jingles.list.well
  (:require
   [om.core :as om :include-macros true]
   [om-tools.dom :as d :include-macros true]
   [om-bootstrap.panel :as p]
   [om-bootstrap.grid :as g]
   [om-bootstrap.random :as r]
   [om-bootstrap.pagination :as pg]
   [om-bootstrap.button :as b]
   [om-bootstrap.input :as i]
   [jingles.match :as jmatch]
   [jingles.list.utils :refer [show-field get-filter-field expand-fields large small]]
   [jingles.utils :refer [goto val-by-id make-event value-by-key menu-items by-id]]
   [jingles.state :refer [set-state! update-state!]]))


(defn list-panel [data owner {:keys [root actions set-filter]}]
  (reify
    om/IDisplayName
    (display-name [_]
      "listpanelc")
    om/IRender
    (render [_]
      (p/panel {:class "list-panel"
                :style (if (:show data) {} {:display :none})
                :header [(:name data)
                         (if actions
                           (d/div {:class "pull-right"}
                                  (b/dropdown {:bs-size "xsmall" :title (r/glyphicon {:glyph "option-vertical"})
                                               :on-click (make-event identity)}
                                              (apply menu-items (actions data)))))]}
               (map
                (fn [field]
                  (d/div
                   (r/glyphicon {:glyph "pushpin"
                                   :class "filterby"
                                   :on-click #(set-filter (str (name (:id field)) ":" (:text field)))})
                   (d/span {:class "field-label"} (:title field) ":")
                   (d/span {:class "value"} (:text field))))
                (:row data))))))

(defn well [data elements {:keys [root actions set-filter]}]
  (d/div
   {:class small}
   (om/build-all list-panel elements {:key :uuid :opts {:root root :actions actions :set-filter set-filter}})))
