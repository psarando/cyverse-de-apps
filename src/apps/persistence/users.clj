(ns apps.persistence.users
  (:use [clojure-commons.core :only [remove-nil-values]]
        [kameleon.uuids :only [uuidify]]
        [korma.core :exclude [update]])
  (:require [korma.core :as sql]
            [apps.util.conversions :refer [long->timestamp]])
  (:import [java.sql Timestamp]))

(defn- user-base-query
  []
  (-> (select* :users)
      (fields :id :username)))

(defn by-id
  [ids]
  (-> (user-base-query)
      (where {:id [in (mapv uuidify ids)]})
      (select)))

(defn for-username
  [username]
  (-> (user-base-query)
      (where {:username username})
      (select)
      (first)))

(defn get-existing-user-id
  "Gets an existing user identifier for a fully qualified username. Returns nil if the
   username isn't found in the database."
  [username]
  ((comp :id first)
   (select :users (where {:username username}))))

(defn- get-new-user-id
  "Gets a new user identifier for a fully qualified username."
  [username]
  (insert :users (values {:username username}))
  (get-existing-user-id username))

(defn get-user-id
  "Gets the internal user identifier for a fully qualified username.  A new
   entry will be added if the user doesn't already exist in the database."
  [username]
  (or (get-existing-user-id username)
      (get-new-user-id username)))

(defn- insert-login-record
  "Records when a user logs into the DE."
  [user-id ip-address session-id login-time]
  (insert :logins
          (values (remove-nil-values
                   {:user_id    user-id
                    :ip_address ip-address
                    :session_id (uuidify session-id)
                    :login_time (long->timestamp login-time)}))))

(defn record-login
  "Records when a user logs into the DE. Returns the recorded login time."
  [username ip-address session-id login-time]
  (-> (insert-login-record (get-user-id username) ip-address session-id login-time)
      (:login_time)
      (.getTime)))
