package web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danon
 */
public class SessionUtils {

    public static boolean isSignedIn() {
        if (SessionListener.getSessionAttribute("userId", false) != null) {
            return true;
        }
        return false;
    }

    public static Long getUserId() {
        Long uId = ((Long) SessionListener.getSessionAttribute("userId", true));
        return uId;
    }

    public static Long getCurrentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("userId");
        return userId;
    }
}
