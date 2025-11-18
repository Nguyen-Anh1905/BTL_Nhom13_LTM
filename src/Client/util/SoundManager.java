package Client.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Media> soundCache = new HashMap<>();
    private boolean soundEnabled = true;
    private MediaPlayer timerWarningPlayer; // Để dừng timer warning khi cần
    
    // MediaPlayer pool cho click char để phát nhanh hơn
    private MediaPlayer[] clickCharPlayers;
    private int currentClickCharIndex = 0;
    private static final int CLICK_CHAR_POOL_SIZE = 5;
    
    // Sound paths
    private static final String SOUND_BASE_PATH = "/Server/assets/sounds/";
    
    public static final String CLICK_CHAR = "game/click_char.mp3";
    public static final String CORRECT_ANSWER = "game/correctAn.mp3";
    public static final String INCORRECT_ANSWER = "game/incorect.mp3";
    public static final String VICTORY = "game/victory_result.mp3";
    public static final String TIMER_WARNING = "game/timer-sound-426781.mp3";
    public static final String BUTTON_CLICK = "ui/computer-mouse-click-02-383961.mp3";
    public static final String NOTIFICATION = "lobby/notification-ping-372479.mp3";
    public static final String BACKGROUND_MUSIC = "lobby/sound_back.mp3";
    public static final String FANFARE = "fanfare-46385.mp3";
    
    private MediaPlayer backgroundMusicPlayer; // Background music player
    
    private SoundManager() {
        // Private constructor for singleton
        // Preload các âm thanh thường dùng để giảm delay
        preloadCommonSounds();
        initClickCharPool();
    }
    
    /**
     * Khởi tạo pool MediaPlayer cho click char để phát nhanh hơn
     */
    private void initClickCharPool() {
        new Thread(() -> {
            try {
                Media clickCharMedia = loadSound(CLICK_CHAR);
                if (clickCharMedia != null) {
                    clickCharPlayers = new MediaPlayer[CLICK_CHAR_POOL_SIZE];
                    for (int i = 0; i < CLICK_CHAR_POOL_SIZE; i++) {
                        clickCharPlayers[i] = new MediaPlayer(clickCharMedia);
                        clickCharPlayers[i].setVolume(0.5);
                        clickCharPlayers[i].setOnEndOfMedia(() -> {
                            // Reset về đầu để có thể phát lại
                        });
                    }
                    System.out.println("✅ Initialized click char player pool");
                }
            } catch (Exception e) {
                System.err.println("❌ Error initializing click char pool: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * Preload các âm thanh thường dùng
     */
    private void preloadCommonSounds() {
        new Thread(() -> {
            loadSound(BUTTON_CLICK);
            loadSound(CLICK_CHAR);
            loadSound(CORRECT_ANSWER);
            loadSound(INCORRECT_ANSWER);
            loadSound(NOTIFICATION);
            System.out.println("✅ Preloaded common sounds");
        }).start();
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Load sound vào cache
     */
    private Media loadSound(String soundPath) {
        if (soundCache.containsKey(soundPath)) {
            return soundCache.get(soundPath);
        }
        
        try {
            URL resource = getClass().getResource(SOUND_BASE_PATH + soundPath);
            if (resource == null) {
                System.err.println("❌ Không tìm thấy sound file: " + SOUND_BASE_PATH + soundPath);
                return null;
            }
            
            Media media = new Media(resource.toExternalForm());
            soundCache.put(soundPath, media);
            System.out.println("✅ Loaded sound: " + soundPath);
            return media;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load sound " + soundPath + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Phát âm thanh - Optimized for fast clicks with player pool
     */
    public void playSound(String soundPath) {
        if (!soundEnabled) return;
        
        // Sử dụng pool cho click char để phát nhanh hơn
        if (soundPath.equals(CLICK_CHAR) && clickCharPlayers != null) {
            try {
                MediaPlayer player = clickCharPlayers[currentClickCharIndex];
                player.stop(); // Dừng nếu đang phát
                player.seek(Duration.ZERO); // Reset về đầu
                player.play();
                
                // Chuyển sang player tiếp theo trong pool
                currentClickCharIndex = (currentClickCharIndex + 1) % CLICK_CHAR_POOL_SIZE;
                return;
            } catch (Exception e) {
                System.err.println("❌ Error with click char pool: " + e.getMessage());
                // Fall through to normal playback
            }
        }
        
        // Phát async cho các âm thanh khác
        new Thread(() -> {
            try {
                Media media = loadSound(soundPath);
                if (media == null) return;
                
                MediaPlayer player = new MediaPlayer(media);
                player.setVolume(0.5);
                player.setOnEndOfMedia(() -> player.dispose());
                player.play();
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi phát sound: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * Phát âm thanh với volume tùy chỉnh
     */
    public void playSound(String soundPath, double volume) {
        if (!soundEnabled) return;
        
        // Phát async để không block UI thread
        new Thread(() -> {
            try {
                Media media = loadSound(soundPath);
                if (media == null) return;
                
                MediaPlayer player = new MediaPlayer(media);
                player.setVolume(Math.max(0.0, Math.min(1.0, volume))); // Clamp 0-1
                player.setOnEndOfMedia(() -> player.dispose());
                player.play();
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi phát sound: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * Phát âm thanh lặp lại (dùng cho timer warning)
     */
    public void startTimerWarning() {
        if (!soundEnabled) return;
        stopTimerWarning(); // Dừng cái cũ nếu đang chạy
        
        try {
            Media media = loadSound(TIMER_WARNING);
            if (media == null) return;
            
            timerWarningPlayer = new MediaPlayer(media);
            timerWarningPlayer.setVolume(0.15);
            timerWarningPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            timerWarningPlayer.play();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi phát timer warning: " + e.getMessage());
        }
    }
    
    /**
     * Dừng âm thanh timer warning
     */
    public void stopTimerWarning() {
        if (timerWarningPlayer != null) {
            timerWarningPlayer.stop();
            timerWarningPlayer.dispose();
            timerWarningPlayer = null;
        }
    }
    
    /**
     * Phát nhạc nền (loop vô hạn)
     */
    public void startBackgroundMusic() {
        if (!soundEnabled) return;
        stopBackgroundMusic(); // Dừng nhạc cũ nếu có
        
        try {
            Media media = loadSound(BACKGROUND_MUSIC);
            if (media == null) return;
            
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setVolume(0.08);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.play();
            System.out.println("🎵 Background music started");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi phát background music: " + e.getMessage());
        }
    }
    
    /**
     * Dừng nhạc nền
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
            System.out.println("🎵 Background music stopped");
        }
    }
    
    /**
     * Bật/tắt âm thanh
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopTimerWarning();
            stopBackgroundMusic();
        }
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Xóa cache
     */
    public void clearCache() {
        soundCache.clear();
        stopTimerWarning();
        stopBackgroundMusic();
    }
}
