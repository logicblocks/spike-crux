(ns spike-crux.core
  (:require [crux.api :as crux]
            [clojure.set :as set]))

(defn create-user [node user]
  (crux/submit-tx node [[:crux.tx/put (set/rename-keys user {:id :crux.db/id})]])
  user)

(defn fetch-user [node id]
  (->
    node
    (crux/db)
    (crux/entity id)
    (set/rename-keys {:crux.db/id :id})))
