

export const useCreateTodoWork = () => {
    const apiUrl = 'http://10.60.50.102:20220/todoworkitem';
    const requestType = 'CreateToDoWork';

    const createTodoWork = (workLayer:number,workInnerId:number,
                            workImportancePriority:number,
                            workEmergencyPriority:number,workTitle:string,workSubtitle:string,
                            workDescription:string,workCreateTime:string,
                            workStartTime:string,workDeadline:string,
                            workStatus:string,workSubToDoWorkItemInnerIdList:any,
                            workPomodoroRecordInnerIdList:any) => {
        const { data, pending, error, refresh } = useFetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                content: JSON.stringify({
                    '@class': 'shared.ToDoWorkItem',
                    layer : workLayer,
                    innerId : workInnerId,
                    importancePriority : workImportancePriority,
                    emergencyPriority : workEmergencyPriority,
                    title : workTitle,
                    subtitle : workSubtitle,
                    description : workDescription,
                    createTime : workCreateTime,
                    startTime : workStartTime,
                    deadLine : workDeadline,
                    status : workStatus,
                    subToDoWorkItemInnerIdList: workSubToDoWorkItemInnerIdList,
                    pomodoroRecordInnerIdList : workPomodoroRecordInnerIdList
                }),
                requestType: requestType,
            }),
        });

        return { data, pending, error, refresh };
    };

    return { createTodoWork };
};
