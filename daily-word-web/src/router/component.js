import HomeView from "@/views/index.vue";
import Login from "@/views/login/index.vue";
import MainView from "@/views/home/index.vue";
import Dictionary from "@/views/dictionary/index.vue";
import Competition from "@/views/competition/index.vue";
import PersonalCenter from "@/views/personalCenter/index.vue";

export const pathArray=[
    {
        path: '/',
        name: 'home',
        component: HomeView
    }, {
        path: '/login',
        name: 'login',
        component: Login
    }, {
        path: '/home',
        name: 'main',
        component: MainView,
        children : [
            {
                path: '/dictionary',
                name: 'dictionary',
                component: Dictionary
            }, {
                path: '/competition',
                name: 'competition',
                component: Competition
            }, {
                path: '/personalCenter',
                name: 'PersonalCenter',
                component: PersonalCenter
            }
        ]
    }
]
