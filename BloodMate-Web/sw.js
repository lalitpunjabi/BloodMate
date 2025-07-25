// BloodMate Service Worker
// This is a placeholder file to prevent 404 errors during registration.
// You can extend this for offline support and caching if needed.

self.addEventListener('install', event => {
  // Skip waiting to activate immediately
  self.skipWaiting();
});

self.addEventListener('activate', event => {
  // Claim clients immediately
  event.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', event => {
  // For now, just pass through all requests
  return;
});
