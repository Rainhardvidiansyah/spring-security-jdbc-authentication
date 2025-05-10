import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 30,
  duration: '10s',
};

export default function () {
  const url = 'http://app:8080/api/v1/users/auth/register';
  //'http://erp-module-rainhard:8080/api/v1/users/auth/register'

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
    'status is 201': (r) => r.status === 201,
  });
}


/*
RESULT:
k6  |
k6  |          /\      Grafana   /‾‾/
k6  |     /\  /  \     |\  __   /  /
k6  |    /  \/    \    | |/ /  /   ‾‾\
k6  |   /          \   |   (  |  (‾)  |
k6  |  / __________ \  |_|\_\  \_____/
k6  |
k6  |      execution: local
k6  |         script: test-registration.js
k6  |         output: -
k6  |
k6  |      scenarios: (100.00%) 1 scenario, 30 max VUs, 40s max duration (incl. graceful stop):
k6  |               * default: 30 looping VUs for 10s (gracefulStop: 30s)
k6  |
k6  |
k6  | running (01.0s), 30/30 VUs, 37 complete and 0 interrupted iterations
k6  | default   [  10% ] 30 VUs  01.0s/10s
k6  |
k6  | running (02.0s), 30/30 VUs, 111 complete and 0 interrupted iterations
k6  | default   [  20% ] 30 VUs  02.0s/10s
k6  |
k6  | running (03.0s), 30/30 VUs, 188 complete and 0 interrupted iterations
k6  | default   [  30% ] 30 VUs  03.0s/10s
k6  |
k6  | running (04.0s), 30/30 VUs, 263 complete and 0 interrupted iterations
k6  | default   [  40% ] 30 VUs  04.0s/10s
k6  |
k6  | running (05.0s), 30/30 VUs, 341 complete and 0 interrupted iterations
k6  | default   [  50% ] 30 VUs  05.0s/10s
k6  |
k6  | running (06.0s), 30/30 VUs, 414 complete and 0 interrupted iterations
k6  | default   [  60% ] 30 VUs  06.0s/10s
k6  |
k6  | running (07.0s), 30/30 VUs, 493 complete and 0 interrupted iterations
k6  | default   [  70% ] 30 VUs  07.0s/10s
k6  |
k6  | running (08.0s), 30/30 VUs, 564 complete and 0 interrupted iterations
k6  | default   [  80% ] 30 VUs  08.0s/10s
k6  |
k6  | running (09.0s), 30/30 VUs, 641 complete and 0 interrupted iterations
k6  | default   [  90% ] 30 VUs  09.0s/10s
k6  |
k6  | running (10.0s), 30/30 VUs, 713 complete and 0 interrupted iterations
k6  | default   [ 100% ] 30 VUs  10.0s/10s
k6  |
k6  |
k6  |   █ TOTAL RESULTS
k6  |
k6  |     checks_total.......................: 744     73.079304/s
k6  |     checks_succeeded...................: 100.00% 744 out of 744
k6  |     checks_failed......................: 0.00%   0 out of 744
k6  |
k6  |     ✓ status is 201
k6  |
k6  |     HTTP
k6  |     http_req_duration.......................................................: avg=407.11ms min=100.35ms med=409.91ms max=744.9ms  p(90)=596.59ms p(95)=638.05ms
k6  |       { expected_response:true }............................................: avg=407.11ms min=100.35ms med=409.91ms max=744.9ms  p(90)=596.59ms p(95)=638.05ms
k6  |     http_req_failed.........................................................: 0.00%  0 out of 744
k6  |     http_reqs...............................................................: 744    73.079304/s
k6  |
k6  |     EXECUTION
k6  |     iteration_duration......................................................: avg=407.3ms  min=100.46ms med=410.01ms max=744.97ms p(90)=596.71ms p(95)=639.49ms
k6  |     iterations..............................................................: 744    73.079304/s
k6  |     vus.....................................................................: 30     min=30       max=30
k6  |     vus_max.................................................................: 30     min=30       max=30
k6  |
k6  |     NETWORK
k6  |     data_received...........................................................: 299 kB 29 kB/s
k6  |     data_sent...............................................................: 167 kB 16 kB/s
k6  |
k6  |
k6  |
k6  |
k6  | running (10.2s), 00/30 VUs, 744 complete and 0 interrupted iterations
k6  | default ✓ [ 100% ] 30 VUs  10s

*/