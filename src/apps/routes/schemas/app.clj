(ns apps.routes.schemas.app
  (:use [apps.routes.params :only [SecuredQueryParams SecuredQueryParamsEmailRequired]]
        [common-swagger-api.schema
         :only [describe
                PagingParams
                SortFieldDocs
                SortFieldOptionalKey]]
        [schema.core :only [defschema enum]])
  (:require [clojure.set :as sets]
            [common-swagger-api.schema.apps :as app-schema]
            [common-swagger-api.schema.apps.admin :as admin-schema]))

(def AdminAppListingValidSortFields
  (sets/union app-schema/AppListingValidSortFields
              admin-schema/AdminAppListingJobStatsKeys))

(defschema AppListingPagingParams
  (merge SecuredQueryParamsEmailRequired
         PagingParams
         app-schema/AppFilterParams
         {SortFieldOptionalKey
          (describe (apply enum app-schema/AppListingValidSortFields)
                    SortFieldDocs)}))

(defschema AppSearchParams
  (merge SecuredQueryParams app-schema/AppSearchParams))

(defschema AdminAppSearchParams
  (merge SecuredQueryParams admin-schema/AdminAppSearchParams))
