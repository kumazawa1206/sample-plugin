package plugin.firstplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLevelCommand implements CommandExecutor {

  //コンストラクタを用いてMainの機能を使えるようにする。
  private First_plugin first_plugin;

  public SetLevelCommand(First_plugin first_plugin) {
    this.first_plugin = first_plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player player) {
      //引数が１個の時、文字列を数字に変換して取得する。
      //引数が１個じゃなければ"Nooooooo!!"を返す。
      if (args.length == 1) {
        player.setLevel(Integer.parseInt(args[0]));
      } else {
        player.sendMessage(first_plugin.getConfig().getString("Message"));
      }
    }
    return false;
  }
}
