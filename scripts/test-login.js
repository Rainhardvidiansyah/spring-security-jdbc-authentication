import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 30,
  duration: '10s', // 30 VUs selama 10 detik
};

export default function () {
  const url = 'http://app:8080/api/v1/users/auth/register'; // app:8080 is a service name, not container name

  const payload = JSON.stringify({
    email: 'test@example.com',
    password: 'password123',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, payload, params);

  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}
