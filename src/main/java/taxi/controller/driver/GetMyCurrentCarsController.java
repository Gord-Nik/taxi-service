package taxi.controller.driver;

import taxi.lib.Injector;
import taxi.model.Car;
import taxi.service.CarService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetMyCurrentCarsController extends HttpServlet {
    private static final String DRIVER_ID = "driver_id";
    private static final Injector injector = Injector.getInstance("taxi");
    private final CarService carService = (CarService) injector.getInstance(CarService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Car> allCars =
                carService.getAllByDriver((Long) req.getSession().getAttribute(DRIVER_ID));
        req.setAttribute("cars", allCars);
        req.getRequestDispatcher("/WEB-INF/views/cars/my.jsp").forward(req,resp);
    }
}
