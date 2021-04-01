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

(defn- load-user-by-id [node id]
  (->
    node
    (crux/db)
    (crux/entity id)))

(defn create-user [node user]
  (crux/submit-tx node [[:crux.tx/put (set/rename-keys user {:id :crux.db/id})]])
  user)

(defn fetch-user-by-id [node id]
  (set/rename-keys
    (load-user-by-id node id)
    {:crux.db/id :id}))

(defn fetch-users-by-last-name [node last-name]
  (->
    node
    (crux/db)
    (crux/q
      {:find          '[element]
       :where         '[[element :last-name n]]
       :args          [{'n last-name}]
       :full-results? true})
    (map-ids)))

(defn set-email [node id email]
  ;; in a proper service you'd obviously not create the transaction function here
  ;; but rather during the initial service setup
  (crux/submit-tx node [[:crux.tx/put
                         {:crux.db/id :set-email
                          :crux.db/fn '(fn [ctx eid mail]
                                         (let [db (crux.api/db ctx)
                                               entity (crux.api/entity db eid)]
                                           [[:crux.tx/put (assoc entity :email mail)]]))}]])
  (crux/submit-tx node [[:crux.tx/fn :set-email id email]]))
