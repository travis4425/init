package controller.PromotionsController;

import dao.PromotionDAO;
import dto.Promotion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.DBUtils;

@WebServlet(name = "PromotionController", urlPatterns = {"/PromotionController"})
public class PromotionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DBUtils.getConnection()) {
            String action = request.getParameter("action");
            PromotionDAO dao = new PromotionDAO(conn);
            List<Promotion> list;
            if ("search".equals(action)) {
                String nameSearch = request.getParameter("nameSearch");
                request.setAttribute("nameSearch", nameSearch);
                list = dao.search(nameSearch);
            } else {
                list = dao.getAllPromotions();
            }
            request.setAttribute("PROMOTION_LIST", list);
            request.getRequestDispatcher("promotion/promotionList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal Server Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DBUtils.getConnection()) {
            String action = request.getParameter("action");
            PromotionDAO dao = new PromotionDAO(conn);

            if ("add".equals(action)) {
                String name = request.getParameter("name");
                int discount = Integer.parseInt(request.getParameter("discountPercent"));
                Date startDate = Date.valueOf(request.getParameter("startDate"));
                Date endDate = Date.valueOf(request.getParameter("endDate"));
                boolean status = Boolean.parseBoolean(request.getParameter("status"));

                Promotion promo = new Promotion(0, name, discount, startDate, endDate, status);
                dao.addPromotion(promo);

            } else if ("delete".equals(action)) {
                int promoID = Integer.parseInt(request.getParameter("promoID"));
                dao.deletePromotion(promoID);

            } else if ("toggleStatus".equals(action)) {
                int promoID = Integer.parseInt(request.getParameter("promoID"));
                boolean newStatus = Boolean.parseBoolean(request.getParameter("newStatus"));
                dao.updateStatus(promoID, newStatus);
            } 

            response.sendRedirect("PromotionController");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
