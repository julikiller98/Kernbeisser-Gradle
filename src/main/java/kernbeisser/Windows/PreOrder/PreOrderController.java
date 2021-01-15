package kernbeisser.Windows.PreOrder;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import javax.persistence.NoResultException;
import javax.swing.*;
import kernbeisser.CustomComponents.BarcodeCapture;
import kernbeisser.CustomComponents.KeyCapture;
import kernbeisser.DBEntities.*;
import kernbeisser.Enums.PermissionKey;
import kernbeisser.Windows.MVC.Controller;
import kernbeisser.Windows.ShoppingMask.ArticleSelector.ArticleSelectorController;
import kernbeisser.Windows.ViewContainers.SubWindow;
import lombok.var;
import org.jetbrains.annotations.NotNull;

public class PreOrderController extends Controller<PreOrderView, PreOrderModel> {

  private final KeyCapture keyCapture;
  private final BarcodeCapture barcodeCapture;

  public PreOrderController() {
    super(new PreOrderModel());
    keyCapture = new KeyCapture();
    barcodeCapture = new BarcodeCapture(this::processBarcode);
  }

  @Override
  public @NotNull PreOrderModel getModel() {
    return model;
  }

  boolean searchKK() {
    PreOrderView view = getView();
    if (view.getKkNumber() != 0) {
      try {
        pasteDataInView(model.getItemByKkNumber(view.getKkNumber()));
        return true;
      } catch (NoResultException e) {
        setDefaultInput();
        return false;
      }
    } else {
      setDefaultInput();
      return false;
    }
  }

  void add() {
    try {
      PreOrder order = obtainFromView();
      if(order.getUser() == null){
        getView().notifyNoUserSelected();
        return;
      }
      model.add(order);
      getView().addPreOrder(order);
      setDefaultInput();
    } catch (NoResultException e) {
      getView().noItemFound();
    }
  }

  void delete(PreOrder preOrder) {
    if (preOrder == null) {
      getView().noPreOrderSelected();
      return;
    }
    model.remove(preOrder);
    getView().remove(preOrder);
  }

  void delete(Collection<PreOrder> preOrders) {
    if (preOrders.size() == 0) {
      getView().noPreOrderSelected();
      return;
    }
    for (PreOrder preOrder : preOrders) {
      model.remove(preOrder);
      getView().remove(preOrder);
    }
  }

  void setDefaultInput() {
    Article empty = new Article();
    empty.setName("Kein Artikel gefunden");
    empty.setSurchargeGroup(new SurchargeGroup());
    pasteDataInView(empty);
    getView().resetArticleNr();
  }

  @Override
  protected boolean commitClose() {
    model.close();
    return true;
  }

  void pasteDataInView(Article articleKornkraft) {
    var view = getView();
    // view.setAmount(String.valueOf(1));
    double containerSize = articleKornkraft.getContainerSize();
    view.setContainerSize(new DecimalFormat("0.###").format(containerSize));
    view.setNetPrice(PreOrderModel.containerNetPrice(articleKornkraft));
    view.setSellingPrice(
        String.format(
            "%.2f€", new ShoppingItem(articleKornkraft, 0, true).getRetailPrice() * containerSize));
    view.setItemName(articleKornkraft.getName());
  }

  private PreOrder obtainFromView() {
    PreOrder preOrder = new PreOrder();
    Article article = findArticle();
    var view = getView();
    preOrder.setUser(view.getUser());
    preOrder.setArticle(article);
    preOrder.setAmount(view.getAmount());
    preOrder.setInfo(article.getInfo());
    return preOrder;
  }

  private Article findArticle() {
    if (getView().getKkNumber() == 0) throw new NoResultException();
    return model.getItemByKkNumber(getView().getKkNumber());
  }

  void insert(Article article) {
    if (article == null) throw new NullPointerException("cannot insert null as PreOrder");
    var view = getView();
    if(view.getUser() == null){
      getView().notifyNoUserSelected();
      return;
    }
    PreOrder preOrder = new PreOrder();
    preOrder.setUser(view.getUser());
    preOrder.setArticle(article);
    preOrder.setAmount(view.getAmount());
    preOrder.setInfo(article.getInfo());

    model.add(preOrder);
    getView().addPreOrder(preOrder);
  }

  void openSearchWindow() {
    new ArticleSelectorController(this::pasteDataInView)
        .withCloseEvent(() -> getView().searchArticle.setEnabled(true))
        .openIn(new SubWindow(getView().traceViewContainer()));
    getView().searchArticle.setEnabled(false);
  }

  @Override
  public void fillView(PreOrderView view) {
    keyCapture.add(KeyEvent.VK_F2, () -> view.fnKeyAction("2"));
    keyCapture.add(KeyEvent.VK_F3, () -> view.fnKeyAction("3"));
    keyCapture.add(KeyEvent.VK_F4, () -> view.fnKeyAction("4"));
    keyCapture.add(KeyEvent.VK_F5, () -> view.fnKeyAction("5"));
    keyCapture.add(KeyEvent.VK_F6, () -> view.fnKeyAction("6"));
    keyCapture.add(KeyEvent.VK_F7, () -> view.fnKeyAction("8"));
    keyCapture.add(KeyEvent.VK_F8, () -> view.fnKeyAction("10"));
    view.setInsertSectionEnabled(PermissionKey.ACTION_ORDER_CONTAINER.userHas());
    view.setUser(User.getAllUserFullNames(true));
    view.setPreOrders(model.getAllPreOrders());
    view.enableControls(false);
    view.setAmount("1");
    view.searchArticle.addActionListener(e -> openSearchWindow());
    setDefaultInput();
  }

  @Override
  protected boolean processKeyboardInput(KeyEvent e) {
    return barcodeCapture.processKeyEvent(e) || keyCapture.processKeyEvent(e);
  }

  private void processBarcode(String s) {
    try {
      insert(model.getByBarcode(s));
    } catch (NoResultException e) {
      getView().noArticleFoundForBarcode(s);
    }
  }

  public void printChecklist() {
    model.printCheckList();
  }

  public void exportPreOrder() {
    PreOrderView view = getView();
    try {
      if (model.exportPreOrder(view.getContent()) == JFileChooser.APPROVE_OPTION) {
        view.messageExportSuccess();
      } else {
        view.messageExportCanceled();
      }
    } catch (IOException e) {
      view.messageExportError(e);
    }
  }
}
