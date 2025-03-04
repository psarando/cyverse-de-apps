(ns apps.routes.callbacks
  (:use [apps.routes.schemas.callback]
        [common-swagger-api.schema]
        [common-swagger-api.schema.analyses :only [AnalysisIdPathParam]]
        [common-swagger-api.schema.callbacks :only [TapisJobStatusUpdate]]
        [ring.util.http-response :only [ok]])
  (:require [apps.service.callbacks :as callbacks]))

(defroutes callbacks
           (POST "/de-job" []
                 :body [body (describe DeJobStatusUpdate "The updated job status information.")]
                 :summary "Update the status of of a DE analysis."
                 :description "The jex-events service calls this endpoint when the status of a DE analysis changes"
                 (ok (callbacks/update-de-job-status body)))

           (POST "/tapis-job/:job-id" []
                 :path-params [job-id :- AnalysisIdPathParam]
                 :body [body (describe TapisJobStatusUpdate "The updated job status information.")]
                 :summary "Update the status of an Tapis analysis."
                 :description "The DE registers this endpoint as a callback when it submts jobs to Tapis."
                 (ok (callbacks/update-tapis-job-status job-id body))))
