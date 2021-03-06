(ns apps.routes.schemas.oauth
  (:use [common-swagger-api.schema :only [describe doc-only]])
  (:require [schema.core :as s]))

(s/defschema OAuthCallbackResponse
  {:state_info (describe String "Arbitrary state information required by the UI.")})

(s/defschema AdminTokenInfo
  {:access_token  (describe String "The access token itself.")
   :expires_at    (describe Long "The token expiration time as milliseconds since the epoch.")
   :refresh_token (describe String "The refresh token to use when the access token expires.")
   :webapp        (describe String "The name of the external web application.")})

(s/defschema TokenInfo
  (select-keys AdminTokenInfo [:expires_at :webapp]))
