(ns spike-crux.core-test
  (:require [clojure.test :refer :all]
            [testit.core :refer :all]
            [spike-crux.core :refer :all]
            [crux.api :as crux]))

(deftest create-user-test
  (testing "returns created user"
    (let [node (crux/start-node {})
          user {:id :123
                :first-name "Bob"
                :last-name  "Andersson"}
          created-user (create-user node user)]
      (is (= user created-user)))))

(deftest fetch-user-test
  (testing "persists user"
    (let [node (crux/start-node {})
          id :123
          user {:id id
                :first-name "Bob"
                :last-name  "Andersson"}
          equal-user? #(= user %)
          _ (create-user node user)]
      (fact
        (fetch-user node id)
        =eventually=>
        equal-user?))))
