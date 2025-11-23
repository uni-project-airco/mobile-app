# PubNub Test Payloads

Use these JSON payloads to test the PubNub notification integration. You can publish them using:
- PubNub Debug Console: https://www.pubnub.com/console/
- PubNub REST API
- Any PubNub SDK

## Channel Name
Default channel: `notifications`

## Test Payloads

### 1. High Priority Alert
```json
{
  "title": "CO₂ Level Critical",
  "message": "CO₂ concentration has reached 1200 ppm. Immediate ventilation required!",
  "status": "high"
}
```

### 2. Warning Alert
```json
{
  "title": "PM2.5 Threshold Exceeded",
  "message": "PM2.5 levels are at 55 μg/m³. Consider reducing outdoor activities.",
  "status": "warning"
}
```

### 3. Info Notification
```json
{
  "title": "Temperature Change Detected",
  "message": "Room temperature has increased by 2°C in the last hour.",
  "status": "info"
}
```

### 4. Success Notification
```json
{
  "title": "Air Quality Improved",
  "message": "All air quality parameters have returned to optimal levels.",
  "status": "success"
}
```

### 5. Alternative Field Names (Title/Message/Status)
```json
{
  "Title": "System Update",
  "Message": "Air quality monitoring system has been updated successfully.",
  "Status": "success"
}
```

### 6. Alternative Field Names (text)
```json
{
  "title": "Sensor Calibration",
  "text": "All sensors have been calibrated and are operating normally.",
  "status": "info"
}
```

### 7. Minimal Payload (will use defaults)
```json
{
  "title": "Quick Alert",
  "message": "This is a test notification"
}
```

### 8. Plain Text (fallback)
```
This is a plain text message that will be displayed as-is
```

## How to Test

### Option 1: Using PubNub Console
1. Go to https://www.pubnub.com/console/
2. Enter your **Subscribe Key** and **Publish Key**
3. Subscribe to channel: `notifications`
4. Publish one of the JSON payloads above
5. You should see the notification appear in your app

### Option 2: Using cURL
```bash
curl -X POST "https://ps.pndsn.com/publish/pub-c-<YOUR_PUBLISH_KEY>/sub-c-<YOUR_SUBSCRIBE_KEY>/0/notifications/0" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Notification",
    "message": "This is a test message from cURL",
    "status": "info"
  }'
```

### Option 3: Using PubNub REST API (with auth)
```bash
curl -X POST "https://ps.pndsn.com/publish/pub-c-<YOUR_PUBLISH_KEY>/sub-c-<YOUR_SUBSCRIBE_KEY>/0/notifications/0?uuid=test-user" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "API Test",
    "message": "Notification sent via REST API",
    "status": "warning"
  }'
```

### Option 4: Using Android Test Code
Add this to your app for testing:

```kotlin
// In your test or debug code
fun testPublishNotification() {
    val pubnub = PubNub(PNConfiguration(UserId("test-publisher")).apply {
        publishKey = "your-publish-key"
        subscribeKey = "your-subscribe-key"
    })
    
    val message = mapOf(
        "title" to "Test Notification",
        "message" to "This is a test from the app",
        "status" to "info"
    )
    
    pubnub.publish(
        channel = "notifications",
        message = message
    ).async { result, status ->
        if (status.error) {
            Log.e("Test", "Publish failed: ${status.errorData?.message}")
        } else {
            Log.d("Test", "Published successfully: ${result.timetoken}")
        }
    }
}
```

## Expected Behavior

When you publish a payload:
1. The app should receive it via PubNub subscription
2. A new notification should appear at the top of the notifications list
3. The notification should have:
   - Correct icon based on status (high/warning/info/success)
   - Title from the payload
   - Message from the payload
   - Status badge with appropriate color
   - "Just now" or relative time
   - Red dot indicator (isNew = true)

## Status Values and Icons

- `"high"` → Red icon (R.drawable.high)
- `"warning"` → Warning icon (R.drawable.warning)
- `"info"` → Info icon (R.drawable.warning_info)
- `"success"` → Checkmark icon (R.drawable.checkmark)
- Any other value → Default info icon

