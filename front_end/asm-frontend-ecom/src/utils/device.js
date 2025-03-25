import FingerprintJS from '@fingerprintjs/fingerprintjs';

export default async function getDeviceFingerprint() {
    const fp = await FingerprintJS.load();
    const result = await fp.get();
    return result.visitorId; // Trả về một ID duy nhất
}
