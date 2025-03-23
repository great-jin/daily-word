import HomeView from "@/views/index.vue";
import LoginView from "@/views/login/index.vue";
import LogoutView from "@/views/logout/index.vue";
import DictionaryView from "@/views/dictionary/index.vue";
import CompetitionView from "@/views/competition/index.vue";
import AnswerView from "@/views/competition/answer/index.vue";
import PersonalView from "@/views/personal/index.vue";

export const pathArray = [
    {
        path: '/',
        redirect: '/dictionary'
    },
    {
        path: '/',
        name: 'Home',
        component: HomeView,
        children: [
            {
                path: '/dictionary',
                name: 'Dictionary',
                component: DictionaryView
            }, {
                path: '/competition',
                name: 'Competition',
                component: CompetitionView
            }, {
                path: '/personal',
                name: 'Personal',
                component: PersonalView
            }, {
                path: '/roomRank',
                name: 'roomRank',
                component: AnswerView
            }, {
                path: '/logout',
                name: 'Logout',
                component: LogoutView
            }
        ]
    }, {
        path: '/login',
        name: 'Login',
        component: LoginView
    }
]
