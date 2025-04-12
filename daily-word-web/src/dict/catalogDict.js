export const CATALOG_ARRAY = [
    {
        label: '四级词汇',
        value: 'CET4',
        score: 5
    },
    {
        label: '六级词汇',
        value: 'CET6',
        score: 10
    },
    {
        label: '考研词汇',
        value: 'Graduate',
        score: 15
    },
    {
        label: 'GRE词汇',
        value: 'GRE',
        score: 20
    },
    {
        label: '牛津词汇',
        value: 'Oxford',
        score: 25
    }
]


export function getCatalog(data) {
    switch (data) {
        case 'CET4':
            return '四级词汇'
        case 'CET6':
            return '六级词汇'
        case 'Graduate':
            return '考研词汇'
        case 'GRE':
            return 'GRE词汇'
        case 'Oxford':
            return '牛津词汇'
        default:
            return '-'
    }
}