import HomeView from "@/views/index.vue";
import LoginView from "@/views/login/index.vue";
import LogoutView from "@/views/logout/index.vue";
import DictionaryView from "@/views/dictionary/index.vue";
import AiTranslation from "@/views/translate/index.vue";
import CompetitionView from "@/views/competition/index.vue";
import RankView from "@/views/ranking/index.vue";
import AnswerView from "@/views/competition/answer/index.vue";
import PersonalView from "@/views/personal/index.vue";

export const pathArray = [
    {
        path: '/',
        redirect: '/dictionary'
    }, {
        path: '/login',
        name: 'Login',
        component: LoginView
    }, {
        path: '/',
        name: 'Home',
        component: HomeView,
        children: [
            {
                path: '/dictionary',
                name: 'Dictionary',
                component: DictionaryView
            }, {
                path: '/aiTranslate',
                name: 'AiTranslation',
                component: AiTranslation
            }, {
                path: '/competition',
                name: 'Competition',
                component: CompetitionView
            }, {
                path: '/rank',
                name: 'Rank',
                component: RankView
            }, {
                path: '/personal',
                name: 'Personal',
                component: PersonalView
            }, {
                path: '/answer',
                name: 'Answer',
                component: AnswerView
            }, {
                path: '/logout',
                name: 'Logout',
                component: LogoutView
            }
        ]
    }
]
