// nuxt项目目录/composables/Pomodoro.ts

let baseUrl = 'http://localhost:20220'

const sendRequest = (body: any) => {
    return useFetch(baseUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
};

export default new class PomodoroRequest {
    start(innerId:number) {
        const requestBody = {
            content: innerId,
            requestType: 'StartPomodoro',
        };
        return sendRequest(requestBody);
    }

    end() {
        const requestBody = {
            content:'',
            requestType: 'EndPomodoro',
        };
        return sendRequest(requestBody);
    }

    edit(workTime:number,restTime:number) {
        const requestBody = {
            content:JSON.stringify({
                '@class': 'shared.PomodoroInfo',
                workTime: workTime,
                restTime : restTime,
            }),
            requestType: 'EditPomodoro',
        };
        return sendRequest(requestBody);
    }

}
