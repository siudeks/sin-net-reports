export const addressProvider = (): { host: string } => {
  const origin = window.location.origin;
  const host = asBackendHost(origin);
  return { host };
}

const asBackendHost = (appHost: string): string => {
  if (origin.endsWith("raport.sin.net.pl")) return appHost;
  if (origin.endsWith("sinnet.local")) return appHost;
  return "https://sinnet.local";
  // return "http://localhost:11010";
}
