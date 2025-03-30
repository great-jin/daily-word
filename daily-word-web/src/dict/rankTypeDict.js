export const TYPE_OPTIONS = [
    {
        label: '单人挑战',
        value: 0,
        describe: '单人挑战不计入排行统计哦'
    }, {
        label: '双人对战',
        value: 2,
        describe: '双人挑战'
    },
    {
        label: '三人对战',
        value: 3,
        describe: '三人挑战'
    },
    {
        label: '五人对战',
        value: 5,
        describe: '五人挑战'
    }
]

export const SIZE_OPTIONS = [
    {
        label: '30个/组',
        value: '30',
        score: 5
    },
    {
        label: '40个/组',
        value: '40',
        score: 10
    },
    {
        label: '50个/组',
        value: '50',
        score: 15
    },
    {
        label: '80个/组',
        value: '80',
        score: 20
    }
]

// 匹配人数
export function getRankType(data) {
    switch (data) {
        case 0:
            return '单人挑战'
        case 2:
            return '双人对战'
        case 3:
            return '三人对战'
        case 5:
            return '五人对战'
        default:
            return '-'
    }
}