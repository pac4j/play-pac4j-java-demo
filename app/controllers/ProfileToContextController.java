package controllers;

import org.pac4j.play.java.ProfileToContext;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Sample for the usage of the {@link org.pac4j.play.java.ProfileToContext} annotation.
 * @author Sebastian Hardt (s.hardt@micromata.de)
 */
@ProfileToContext
public class ProfileToContextController extends Controller
{

  /**
   * Displays the pag1 tpl
   * @return the pag1 html view
   */
  public Result displayPage1() {
    return ok(views.html.page1.render());
  }

}
