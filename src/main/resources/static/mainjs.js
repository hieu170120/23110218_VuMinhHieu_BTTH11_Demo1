// mainjs.js

document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('loginForm')) {
        document.getElementById('loginForm').onsubmit = login;
    }
    if (document.getElementById('registerForm')) {
        document.getElementById('registerForm').onsubmit = register;
    }
    if (document.getElementById('profileInfo')) {
        loadProfile();
    }
});

function showRegister() {
    document.getElementById('registerSection').style.display = 'block';
}

function login(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    fetch('/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
    .then(res => res.ok ? res.json() : res.text().then(text => Promise.reject(text)))
    .then(data => {
        localStorage.setItem('jwt', data.token);
        window.location = 'profile.html';
    })
    .catch(err => {
        document.getElementById('loginError').innerText = err;
    });
}

function register(e) {
    e.preventDefault();
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;
    const name = document.getElementById('regName').value;
    fetch('/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password, name })
    })
    .then(res => res.ok ? res.text() : res.text().then(text => Promise.reject(text)))
    .then(msg => {
        document.getElementById('registerSuccess').innerText = msg;
        document.getElementById('registerError').innerText = '';
    })
    .catch(err => {
        document.getElementById('registerError').innerText = err;
        document.getElementById('registerSuccess').innerText = '';
    });
}

function loadProfile() {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) {
        window.location = 'login.html';
        return;
    }
    fetch('/users/me', {
        headers: { 'Authorization': 'Bearer ' + jwt }
    })
    .then(res => res.ok ? res.json() : Promise.reject('Unauthorized'))
    .then(user => {
        document.getElementById('profileInfo').innerText = JSON.stringify(user, null, 2);
    })
    .catch(() => {
        localStorage.removeItem('jwt');
        window.location = 'login.html';
    });
}

function logout() {
    localStorage.removeItem('jwt');
    window.location = 'login.html';
}
