export function isEmail(email) {
    return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
}

export function formatSeconds(seconds) {
    if (isNaN(seconds) || seconds < 0) return "00:00:00";

    let hours = Math.floor(seconds / 3600);
    let minutes = Math.floor((seconds % 3600) / 60);
    let secs = seconds % 60;

    // 补零格式化 (确保两位数)
    return [hours, minutes, secs].map(num => String(num).padStart(2, '0')).join(':');
}
