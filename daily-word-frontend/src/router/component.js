import HomeView from "@/views/index.vue";
import MainView from "@/views/home/index.vue";
import DailyPlan from "@/views/dailyPlan/index.vue";
import Competition from "@/views/competition/index.vue";
import ScoreCenter from "@/views/scoreCenter/index.vue";
import PersonalCenter from "@/views/personalCenter/index.vue";

export const pathArray=[
    {
        path: '/',
        name: 'home',
        component: HomeView
    }, {
        path: '/home',
        name: 'main',
        component: MainView,
        children : [
            {
                path: '/dailyPlan',
                name: 'dailyPlan',
                component: DailyPlan
            }, {
                path: '/competition',
                name: 'competition',
                component: Competition
            }, {
                path: '/scoreCenter',
                name: 'scoreCenter',
                component: ScoreCenter
            }, {
                path: '/personalCenter',
                name: 'PersonalCenter',
                component: PersonalCenter
            }
        ]
    }
]
