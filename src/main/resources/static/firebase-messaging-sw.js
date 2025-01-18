importScripts("https://www.gstatic.com/firebasejs/9.21.0/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/9.21.0/firebase-messaging.js");

const firebaseConfig = {
    apiKey: "AIzaSyBCHjGjYjeHJ_BfnYr6971MNAmLmedWKa8",
    authDomain: "early-table-6315a.firebaseapp.com",
    projectId: "early-table-6315a",
    storageBucket: "early-table-6315a.firebasestorage.app",
    messagingSenderId: "531693985268",
    appId: "1:531693985268:web:404dd20c73d5d90e416da1",
    measurementId: "G-2K9VLFMZK3"
};

// Initialize Firebase in the service worker
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

// Handle background messages
messaging.onBackgroundMessage((payload) => {
    console.log("Received background message ", payload);
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: "/firebase-logo.png"
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
