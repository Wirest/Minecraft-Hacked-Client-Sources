package rip.autumn.alt;

public final class Alt {
   private final String email;
   private final String password;
   private String username;

   public Alt(String email, String password) {
      this.email = email;
      this.password = password;
      this.username = email;
   }

   public String getEmail() {
      return this.email;
   }

   public String getPassword() {
      return this.password;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
