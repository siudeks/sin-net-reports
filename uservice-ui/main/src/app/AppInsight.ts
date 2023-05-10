import { ApplicationInsights, DistributedTracingModes } from '@microsoft/applicationinsights-web';
import { ReactPlugin } from '@microsoft/applicationinsights-react-js';
import { createBrowserHistory } from 'history';
import { workMode, WORK_MODE } from './configuration/Configuration';

const browserHistory = createBrowserHistory({ basename: '' });
const reactPlugin = new ReactPlugin();

// configuration details: https://docs.microsoft.com/en-us/azure/azure-monitor/app/javascript
const appInsights = new ApplicationInsights({
    config: {
        instrumentationKey: 'a0599196-9031-45c9-9521-b065ce853f0b',
        extensions: [reactPlugin],
        extensionConfig: {
          [reactPlugin.identifier]: { history: browserHistory }
        },

        // Correlation generates and sends data that enables distributed tracing and powers the application map,
        // end-to-end transaction view, and other diagnostic tools.
        // https://docs.microsoft.com/en-us/azure/azure-monitor/app/javascript#enable-correlation
        disableFetchTracking: false,
        enableCorsCorrelation: true,
        enableRequestHeaderTracking: true,
        enableResponseHeaderTracking: true,

        // https://docs.microsoft.com/en-us/azure/azure-monitor/app/correlation#enable-w3c-distributed-tracing-support-for-web-apps
        distributedTracingMode: DistributedTracingModes.W3C,

        // Use enableAutoRouteTracking: true only if you are not using the React plugin.
        // Both are capable of sending new PageViews when the route changes. If both are enabled, duplicate PageViews may be sent.
        enableAutoRouteTracking: false,

        // TODO read more about excluding correlation headers
        // correlationHeaderExcludedDomains: config.THIRD_PARTY_DOMAINS
      }
});

// we don't want to have AppInsight measured from localhost
// in the same way appinsight agent is run only on k8s ebv so backend on lcoalhost is also not monitored
if (workMode() === WORK_MODE.PROD) {
  appInsights.loadAppInsights();
  appInsights.addTelemetryInitializer(telemetryItem => {
    // ai.cloud.role 
    // more @ https://docs.microsoft.com/en-us/azure/azure-monitor/app/app-map?tabs=net#understanding-cloud-role-name-within-the-context-of-the-application-map
    telemetryItem.tags!['ai.cloud.role'] = 'sinnet-webapp';
  })
}


appInsights.trackPageView();

export { reactPlugin, appInsights };