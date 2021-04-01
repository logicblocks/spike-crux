(ns spike-crux.core
  (:require [crux.api :as crux]
            [clojure.set :as set]))

(defn- map-id [user]
  (set/rename-keys user {:crux.db/id :id}))

(defn- map-ids [users]
  (map #(->
          %
          first
          map-id)
       users))

(defn create-user [node user]
  (crux/submit-tx node [[:crux.tx/put (set/rename-keys user {:id :crux.db/id})]])
  user)

(defn fetch-user-by-id [node id]
  (->
    node
    (crux/db)
    (crux/entity id)
    (set/rename-keys {:crux.db/id :id})))

(defn fetch-users-by-last-name [node last-name]
  (->
    node
    (crux/db)
    (crux/q
      {:find          '[element]
       :where         '[[element :last-name n]]
       :args          [{'n last-name}]
       :full-results? true})
    (map-ids)
    ))
