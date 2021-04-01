(ns spike-crux.core-test
  (:require [clojure.test :refer :all]
            [testit.core :refer :all]
            [spike-crux.core :refer :all]
            [crux.api :as crux]))

(deftest create-user-test
  (testing "returns created user"
    (let [node (crux/start-node {})
          user {:id         :123
                :first-name "Bob"
                :last-name  "Andersson"}
          created-user (create-user node user)]
      (is (= user created-user)))))

(deftest fetch-user-by-id-test
  (testing "persists user"
    (let [node (crux/start-node {})
          id :123
          user {:id         id
                :first-name "Bob"
                :last-name  "Andersson"}
          equal-user? #(= user %)
          _ (create-user node user)]
      (fact
        (fetch-user-by-id node id)
        =eventually=>
        equal-user?))))

(deftest fetch-users-by-last-name-test
  (testing "returns single user"
    (let [node (crux/start-node {})
          last-name "Andersson"
          bob {:id         :123
               :first-name "Bob"
               :last-name  last-name}
          ted {:id         :456
               :first-name "Ted"
               :last-name  "Tedsson"}
          equal-bob? #(= [bob] %)]
      (create-user node bob)
      (create-user node ted)
      (fact
        (fetch-users-by-last-name node last-name)
        =eventually=>
        equal-bob?)))

  (testing "returns multiple users"
    (let [node (crux/start-node {})
          last-name "Andersson"
          bob {:id         :123
               :first-name "Bob"
               :last-name  last-name}
          ted {:id         :456
               :first-name "Ted"
               :last-name  last-name}
          equal-users? #(= [ted bob] %)]
      (create-user node bob)
      (create-user node ted)
      (fact
        (fetch-users-by-last-name node last-name)
        =eventually=>
        equal-users?))))
