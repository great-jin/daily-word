export const MODE_OPTIONS = [
    {
        label: '随机匹配',
        value: 'RANDOM'
    }, {
        label: '自定义房间',
        value: 'CUSTOM'
    }
]

// 匹配方式
export function getRankMode(data) {
    switch (data) {
        case 'RANDOM':
            return '随机匹配'
        case 'CUSTOM':
            return '自定义房间'
        default:
            return '/'
    }
}