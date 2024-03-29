package plugin.firstplugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class First_plugin extends JavaPlugin implements Listener {

  private int count;

  @Override
  public void onEnable() {
    //config.ymlを生成して設定ファイルを読み取るための御作法
    saveDefaultConfig();
    getConfig().getString("Message");

    Bukkit.getPluginManager().registerEvents(this, this);

    //"setLeve"を実行するとSetLevelCommandのonCommandを実行する。
    //plugin.ymlにコマンドを入力する。
    getCommand("setLevel").setExecutor(new SetLevelCommand(this));

    //"allSetLeve"を実行するとAllSetLevelCommandのonCommandを実行する。
    //plugin.ymlにコマンドを入力する。
    getCommand("allSetLevel").setExecutor(new AllSetLevelCommand());
  }

  //Joinした時にEntityが発生する
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    World world = player.getWorld();
    Location playerLocation = player.getLocation();

    //world.spawn(new Location(world, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ()), Pig.class);

    world.getBlockAt(
        new Location(world,
            playerLocation.getX() + 3,
            playerLocation.getY(),
            playerLocation.getZ())).setType(Material.BEACON);
  }


  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    //素数判定
    //BigInteger val = new BigInteger(String.valueOf(this.count));
    //if (val.isProbablePrime(5)) {
    //  System.out.println(val + "は素数です");

    List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.PURPLE);
    if (count % 4 == 0) {
      for (Color color : colorList) {

        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.BALL_LARGE)
                .withFlicker()
                .build());
        fireworkMeta.setPower((1 + 2));

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
      //花火打ち上げ時にメッセージを表示する。
      Path path = Path.of("firework.text");
      Files.writeString(path, "キレーだねー");
      player.sendMessage(Files.readString(path));
    }
    count++;
  }

  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks).filter(
            item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
        .forEach(item -> item.setAmount(20));

    player.getInventory().setContents(itemStacks);
  }

  //PlayerがJoinしたらMessageを表示する。
  //@EventHandler
  //public void PlayerJoinEvent(PlayerJoinEvent e) {
  //  Player player = e.getPlayer();
  //  e.setJoinMessage(player.getName() + "が参加しました");
  //}
}
